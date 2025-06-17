import { NextResponse } from "next/server";
import { Person } from "../types/Person";
import { PersonFormSchema } from "@/schemas/personSchema";

export async function getPersonByIdNumber(idNumber: string): Promise<Person> {
  const response = await fetch(
    `http://localhost:8080/api/titulares/id/${idNumber}`,
    {
      method: "GET",
      credentials: "include",
    }
  );
  /*NextResponse.json(
{
  "id": "1",
  "idNumber": "12345678",
  "firstName": "Juan",
  "lastName": "Pérez",
  "dateOfBirth": "1990-04-10",
  "address": "123 Main St",
  "bloodType": "A+",
  "donor": true,
  "allowedLicenseTypes": ["A", "B", "C"]
});*/
  if (!response.ok) {
    throw new Error("Titular no encontrado");
  }
  return await response.json();
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
