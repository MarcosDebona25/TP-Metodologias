export type Person = {
  id: string;
  numeroDocumento: string;
  nombre: string;
  apellido: string;
  fechaNacimiento: string;
  domicilio: string;
  grupoFactor: string;
  donanteOrganos: string;
  allowedLicenseTypes: string[];
};

export interface PersonWithLicense extends Person {
  currentLicenseTypes: string[];
  fechaEmisionLicencia: string;
  fechaVencimientoLicencia: string;
  observaciones: string;
}
