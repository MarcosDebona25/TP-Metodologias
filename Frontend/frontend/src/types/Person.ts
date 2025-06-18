export type Person = {
  id: string;
  idNumber: string;
  firstName: string;
  lastName: string;
  dateOfBirth: string;
  address: string;
  bloodType: string;
  donor: string;
  allowedLicenseTypes: string[];
  currentLicenseTypes: string[];
};

export interface PersonWithLicense extends Person {
  licenseGrantDate: string;
  licenseExpirationDate: string;
  observaciones: string;
}
