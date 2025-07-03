// src/utils/auth.ts
import jwtDecode from "jwt-decode";

export interface TokenData {
  sub: string; // ID del usuario
  rol: string; // Rol (ADMIN, USER, etc)
  exp: number; // Tiempo de expiración UNIX
  iat: number; // Tiempo de emisión
  [key: string]: any; // Otros posibles campos
}

// Función para obtener el token desde cookies y decodificarlo
export function getTokenData(): TokenData | null {
  if (typeof window === "undefined") return null;

  const token = localStorage.getItem("token"); // o sessionStorage, según tu app

  if (!token) return null;

  try {
    const decoded = jwtDecode<TokenData>(token);
    return decoded;
  } catch (err) {
    console.error("Token inválido:", err);
    return null;
  }
}
