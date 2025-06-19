import { NextResponse } from "next/server";
import { LicenseFormSchema } from "@/schemas/licenseSchema";
import { LicenciaActiva } from "@/types/License";

export async function fetchExpiringLicenses(
  from: string,
  to: string
): Promise<LicenciaActiva[]> {
  let response = await fetch(
    `http://localhost:8080/api/licencias/expiradas?desde=${from}&hasta=${to}`,
    {
      method: "GET",
      credentials: "include",
    }
  );
  if (!response.ok) {
    const errorText = await response.text();
    if (errorText.includes("DNI")) {
      const emptyList: any[] = [];
      response = NextResponse.json(emptyList);
    } else {
      throw new Error("Titular no encontrado");
    }
  }

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

// src/services/licenseService.ts

export async function getLicenseById(idNumber: string) {
  const response = await fetch(
    `http://localhost:8080/api/titulares/id/${idNumber}`,
    {
      method: "GET",
      credentials: "include",
      cache: "no-store",
    }
  );

  if (!response.ok) throw new Error("No se pudo obtener la licencia");

  const data = await response.json();

  return {
    ...data,
    allowedLicenseTypes: data.allowedLicenseTypes
      ? data.allowedLicenseTypes.split(" ").filter(Boolean)
      : [],
  };
}
