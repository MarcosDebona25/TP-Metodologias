import { NextResponse } from "next/server";
import { Person, PersonWithLicense } from "../types/Person";
import { PersonFormSchema } from "@/schemas/personSchema";

export async function getPersonByIdNumber(idNumber: string): Promise<Person> {
  const response = await fetch(
    `http://localhost:8080/api/titulares/id/${idNumber}`,
    //`http://localhost:3000/api/people/${idNumber}`
    {
      method: "GET",
      credentials: "include",
    }
  );
  if (!response.ok) {
    throw new Error("Titular no encontrado");
  }
  const data = await response.json();

  return {
    ...data.titular,
    clases: data.clases ? data.clases.split(" ").filter(Boolean) : [],
  };
}

export async function getPersonWithLicenseByIdNumber(
  idNumber: string
): Promise<PersonWithLicense> {
  const personResponse = await fetch(
    `http://localhost:8080/api/titulares/id/${idNumber}`,
    //`http://localhost:3000/api/licenses/${idNumber}`
    {
      method: "GET",
      credentials: "include",
    }
  );
  if (!personResponse.ok) {
    throw new Error("Titular no encontrado");
  }
  const person = await personResponse.json();

  let licenseResponse = await fetch(
    `http://localhost:8080/api/licencias/${idNumber}`,
    //`http://localhost:3000/api/licenses/${idNumber}`
    {
      method: "GET",
      credentials: "include",
    }
  );
  if (!licenseResponse.ok) {
    const errorText = await licenseResponse.text();
    if (errorText.includes("DNI")) {
      const emptyLicense = {
        currentLicenseTypes: "",
        observaciones: "",
        licenseGrantDate: "",
        licenseExpirationDate: "",
      };
      licenseResponse = NextResponse.json(emptyLicense);
    } else {
      throw new Error("Titular no encontrado");
    }
  }

  const license = await licenseResponse.json();

  return {
    ...person.titular,
    allowedLicenseTypes: person.clases
      ? person.clases.split(" ").filter(Boolean)
      : [],
    ...license,
    currentLicenseTypes: license.clases
      ? license.clases.split(" ").filter(Boolean)
      : [],
  };
}

export async function updatePerson(data: PersonFormSchema): Promise<void> {
  const response = await fetch(
    `http://localhost:8080/api/titulares/${data.numeroDocumento}`,
    {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    }
  );

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText || "Error al actualizar persona");
  }
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
