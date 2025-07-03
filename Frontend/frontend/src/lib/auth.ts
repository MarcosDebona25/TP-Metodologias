export function getTokenData() {
  if (typeof window === "undefined") return null;

  const token = localStorage.getItem("token");
  const rol = localStorage.getItem("rol");

  if (!token || !rol) return null;

  return { token, rol };
}
