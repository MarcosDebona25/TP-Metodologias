import { NextResponse } from "next/server";
import { Person } from "../types/Person";

export async function getPersonByIdNumber(idNumber: string): Promise<Person> {
  const response = await fetch(`http://localhost:8080/api/titulares/id/${idNumber}`, {
    method: "GET",
    credentials: "include",
  });
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
    throw new Error("Person not found");
  }
  return await response.json();
}
