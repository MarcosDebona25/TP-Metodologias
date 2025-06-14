import {Person} from "./Person";

export type LicenseFormData = {
  person: Person | null;
  selectedLicenseTypes: string[];
};
