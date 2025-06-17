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
