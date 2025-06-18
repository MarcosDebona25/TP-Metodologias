import { Person, PersonWithLicense } from "../types/Person";
import { PersonFormSchema } from "@/schemas/personSchema";

export async function getPersonByIdNumber(idNumber: string): Promise<Person> {
  const response = await fetch(
    //`http://localhost:8080/api/titulares/id/${idNumber}`,
    `http://localhost:3000/api/people/${idNumber}`
    //{
    //method: "GET",
    //credentials: "include",
    //}
  );
  if (!response.ok) {
    throw new Error("Titular no encontrado");
  }
  const data = await response.json();

  return {
    ...data,
    allowedLicenseTypes: data.allowedLicenseTypes
      ? data.allowedLicenseTypes.split(" ").filter(Boolean)
      : [],
    currentLicenseTypes: data.currentLicenseTypes
      ? data.currentLicenseTypes.split(" ").filter(Boolean)
      : [],
  };
}

export async function getPersonWithLicenseByIdNumber(
  idNumber: string
): Promise<PersonWithLicense> {
  const response = await fetch(
    //`http://localhost:8080/api/licencias/dni/${idNumber}`,
    `http://localhost:3000/api/licenses/${idNumber}`
    //{
    //method: "GET",
    //credentials: "include",
    //}
  );
  if (!response.ok) {
    throw new Error("Titular no encontrado");
  }
  const data = await response.json();

  return {
    ...data,
    allowedLicenseTypes: data.allowedLicenseTypes
      ? data.allowedLicenseTypes.split(" ").filter(Boolean)
      : [],
    currentLicenseTypes: data.currentLicenseTypes
      ? data.currentLicenseTypes.split(" ").filter(Boolean)
      : [],
  };
}
export async function submitPerson(data: PersonFormSchema): Promise<void> {
  const response = await fetch(`http://localhost:8080/api/titulares`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText || "Error creando titular");
  }
}
