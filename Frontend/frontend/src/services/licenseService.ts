import { LicenseFormSchema } from "@/schemas/licenseSchema";
import { LicenseSummary } from "@/types/LicenseSummary";

export async function fetchExpiringLicenses(
  from: string,
  to: string
): Promise<LicenseSummary[]> {
  const response = await fetch(
    `/api/licencias/expiradas?desde=${from}&hasta=${to}`
  );
  if (!response.ok) {
    throw new Error("Error obteniendo licencias");
  }
  return response.json();
}

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
