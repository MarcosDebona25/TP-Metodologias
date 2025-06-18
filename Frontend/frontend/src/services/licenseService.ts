import { LicenseFormSchema } from "@/schemas/licenseSchema";

export async function submitLicense(data: LicenseFormSchema): Promise<void> {
  const clases = data.clases.join(" ");
  const payload = {
    documentoTitular: data.documentoTitular,
    clases: clases,
    observaciones: data.observaciones,
  };
  const response = await fetch("http://localhost:8080/api/licencias", {
    method: "POST",
    credentials: "include",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText || "Error al crear licencia");
  }
}
