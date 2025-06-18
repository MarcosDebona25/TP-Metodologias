import { LicenseFormSchema } from "@/schemas/licenseSchema";

export async function submitLicense(data: LicenseFormSchema): Promise<void> {
  const response = await fetch("http://localhost:8080/api/licencias", {
    method: "POST",
    credentials: "include",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });

  if (!response.ok) {
  const errorText = await response.text();
    throw new Error(errorText || "Error al crear licencia");
  }
}

// src/services/licenseService.ts

export async function getLicenseById(idNumber: string) {
  const response = await fetch(`http://localhost:8080/api/titulares/id/${idNumber}`, {
    method: "GET",
    credentials: "include",
    cache: "no-store"
  })

  if (!response.ok) throw new Error("No se pudo obtener la licencia")

  const data = await response.json();

  return {
    ...data,
    allowedLicenseTypes: data.allowedLicenseTypes
        ? data.allowedLicenseTypes.split(" ").filter(Boolean)
        : [],
  };
}
