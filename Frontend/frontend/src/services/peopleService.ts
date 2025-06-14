import { Person } from "../types/Person";

export async function getPersonByIdNumber(idNumber: string): Promise<Person> {
  const response = await fetch(`/api/people/${idNumber}`);
  if (!response.ok) {
    throw new Error("Person not found");
  }
  return await response.json();
}
