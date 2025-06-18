export type LicenseSubmission = {
  personId: string;
  licenseTypes: string[];
};
export interface LicenciaActiva{
  documentoTitular: string;
  nombreTitular: string;
  apellidoTitular: string;
  clases: string;
  domicilioTitular: string;
  fechaEmisionLicencia: string;     // formato ISO: "YYYY-MM-DD"
  fechaVencimientoLicencia: string; // formato ISO: "YYYY-MM-DD"
  grupoFactor: string;
  donanteOrganos: string;           // puede ser "Sí" / "No", ajustalo según backend
  observacionesLicencia: string;
}

