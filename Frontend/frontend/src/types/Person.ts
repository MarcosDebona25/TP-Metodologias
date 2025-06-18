export type Person = {
  id: string;
  numeroDocumento: string;
  nombre: string;
  apellido: string;
  fechaNacimiento: string;
  domicilio: string;
  grupoFactor: string;
  donanteOrganos: string;
  clasesPermitidas: string[];
};

export interface PersonWithLicense extends Person {
  currentLicenseTypes: string[];
  licenseGrantDate: string;
  licenseExpirationDate: string;
  observaciones: string;
}
