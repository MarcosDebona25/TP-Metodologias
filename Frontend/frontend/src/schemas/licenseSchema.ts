import { z } from "zod";

export const licenseFormSchema = z.object({
  documentoTitular: z
    .string()
    .length(8, "DNI debe tener 8 dígitos de longitud")
    .regex(/^\d+$/, "DNI debe contener solo dígitos numéricos"),
  observaciones: z.string().optional().or(z.literal("")),
  clases: z
    .array(z.string())
    .min(1, "Seleccione al menos una clase de licencia"),
});

export type LicenseFormSchema = z.infer<typeof licenseFormSchema>;
