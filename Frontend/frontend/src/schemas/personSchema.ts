// schemas/personSchema.ts
import { z } from "zod";

export const personFormSchema = z.object({
  numeroDocumento: z
    .string()
    .length(8, { message: "El número de DNI debe tener 8 dígitos" })
    .regex(/^\d+$/, { message: "Solo se permiten números" }),

  nombre: z
    .string()
    .min(1, { message: "El nombre es obligatorio" })
    .max(50, { message: "El nombre es demasiado largo" }),

  apellido: z
    .string()
    .min(1, { message: "El apellido es obligatorio" })
    .max(50, { message: "El apellido es demasiado largo" }),

  fechaNacimiento: z.string().refine((val) => !isNaN(Date.parse(val)), {
    message: "Fecha inválida",
  }),

  domicilio: z
    .string()
    .min(5, { message: "La dirección es muy corta" })
    .max(100, { message: "La dirección es demasiado larga" }),

  grupoFactor: z.enum(["A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"], {
    errorMap: () => ({ message: "Selecciona un grupo sanguíneo válido" }),
  }),

  donanteOrganos: z.boolean(),
});

export type PersonFormSchema = z.infer<typeof personFormSchema>;
