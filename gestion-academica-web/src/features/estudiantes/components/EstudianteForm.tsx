import { useRef, useState, type FormEvent, type ReactNode } from 'react';
import { esReintentable } from '../../../shared/api/errores';
import { CarreraSelect } from '../../carreras/components/CarreraSelect';
import type { Carrera } from '../../carreras/types';
import type { CampoEstudiante, EstudianteRequest } from '../types';
import {
  aRequest,
  erroresDesdeApi,
  validarEstudiante,
  type ErroresCampo,
  type ValoresFormulario,
} from '../validation/estudianteValidation';

interface Props {
  valoresIniciales: ValoresFormulario;
  textoEnviar: string;
  /** La mutación la ejecuta el contenedor; el formulario solo espera su resultado. */
  onGuardar: (datos: EstudianteRequest) => Promise<unknown>;
  pendiente: boolean;
  onCancelar: () => void;
  carreras: {
    data: Carrera[] | undefined;
    cargando: boolean;
    error: Error | null;
    reintentar: () => void;
  };
}

const ORDEN_CAMPOS: CampoEstudiante[] = ['nombre', 'correo', 'edad', 'carrera'];

/**
 * Formulario presentacional. Su estado (valores escritos, errores, intento de envío) es ESTADO LOCAL:
 * pertenece solo a este formulario y no se comparte ni se guarda en la caché.
 * Ante un error (de validación, de la API o de red) los valores escritos se conservan.
 */
export function EstudianteForm({ valoresIniciales, textoEnviar, onGuardar, pendiente, onCancelar, carreras }: Props) {
  const [valores, setValores] = useState<ValoresFormulario>(valoresIniciales);
  const [errores, setErrores] = useState<ErroresCampo>({});
  const [errorGeneral, setErrorGeneral] = useState<string | null>(null);
  const [puedeReintentar, setPuedeReintentar] = useState(false);
  const [intentado, setIntentado] = useState(false);
  const formRef = useRef<HTMLFormElement>(null);

  function cambiar(campo: CampoEstudiante, valor: string) {
    const siguientes = { ...valores, [campo]: valor };
    setValores(siguientes);
    // Tras el primer intento se revalida el campo mientras se escribe (y se limpia el error de la API de ese campo).
    if (intentado) setErrores((prev) => ({ ...prev, [campo]: validarEstudiante(siguientes)[campo] }));
  }

  function enfocarPrimerError(errs: ErroresCampo) {
    const primero = ORDEN_CAMPOS.find((c) => errs[c]);
    if (primero) formRef.current?.querySelector<HTMLElement>(`#est-${primero}`)?.focus();
  }

  async function onSubmit(e: FormEvent) {
    e.preventDefault();
    if (pendiente) return; // evita envíos repetidos aunque se pulse Enter varias veces
    setIntentado(true);

    const errs = validarEstudiante(valores);
    setErrores(errs);
    if (Object.keys(errs).length > 0) {
      setErrorGeneral('Revisa los campos marcados.');
      setPuedeReintentar(false);
      enfocarPrimerError(errs);
      return;
    }

    setErrorGeneral(null);
    setPuedeReintentar(false);
    try {
      await onGuardar(aRequest(valores));
    } catch (err) {
      const { campos, general } = erroresDesdeApi(err);
      setErrores(campos);
      setErrorGeneral(general);
      setPuedeReintentar(esReintentable(err));
      enfocarPrimerError(campos);
    }
  }

  const sinCarreras = !carreras.cargando && carreras.data?.length === 0;

  const aria = (id: CampoEstudiante) => ({
    'aria-invalid': errores[id] ? true : undefined,
    'aria-describedby': errores[id] ? `est-${id}-error` : undefined,
  });

  function campo(id: CampoEstudiante, etiqueta: string, control: ReactNode) {
    return (
      <div className={errores[id] ? 'campo campo-invalido' : 'campo'}>
        <label htmlFor={`est-${id}`}>{etiqueta}</label>
        {control}
        {errores[id] && (
          <p className="error-campo" id={`est-${id}-error`}>
            {errores[id]}
          </p>
        )}
      </div>
    );
  }

  return (
    <form ref={formRef} onSubmit={onSubmit} noValidate className="formulario">
      {errorGeneral && (
        <div className="aviso-error" role="alert">
          {errorGeneral}
          {puedeReintentar && <span> Tus datos se conservaron; puedes reintentar.</span>}
        </div>
      )}

      <fieldset disabled={pendiente}>
        {campo(
          'nombre',
          'Nombre completo',
          <input
            id="est-nombre"
            value={valores.nombre}
            onChange={(e) => cambiar('nombre', e.target.value)}
            autoComplete="off"
            {...aria('nombre')}
          />,
        )}
        {campo(
          'correo',
          'Correo',
          <input
            id="est-correo"
            type="email"
            value={valores.correo}
            onChange={(e) => cambiar('correo', e.target.value)}
            autoComplete="off"
            {...aria('correo')}
          />,
        )}
        {campo(
          'edad',
          'Edad',
          <input
            id="est-edad"
            type="number"
            inputMode="numeric"
            min={1}
            value={valores.edad}
            onChange={(e) => cambiar('edad', e.target.value)}
            {...aria('edad')}
          />,
        )}
        {campo(
          'carrera',
          'Carrera',
          <CarreraSelect
            id="est-carrera"
            value={valores.carrera}
            onChange={(v) => cambiar('carrera', v)}
            carreras={carreras.data}
            cargando={carreras.cargando}
            error={carreras.error}
            onReintentar={carreras.reintentar}
            invalido={!!errores.carrera}
            describedBy={errores.carrera ? 'est-carrera-error' : undefined}
          />,
        )}
      </fieldset>

      <div className="acciones">
        <button type="button" className="secundario" onClick={onCancelar} disabled={pendiente}>
          Cancelar
        </button>
        <button type="submit" disabled={pendiente || sinCarreras || !!carreras.error}>
          {pendiente ? 'Guardando…' : puedeReintentar ? 'Reintentar' : textoEnviar}
        </button>
      </div>
    </form>
  );
}
