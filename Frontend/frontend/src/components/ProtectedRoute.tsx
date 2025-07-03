"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { getTokenData } from "@/lib/auth";

type Props = {
  children: React.ReactNode;
  requiredRole?: "ADMIN" | "USER"; // 👈 nuevo prop opcional
};

export default function ProtectedRoute({ children, requiredRole }: Props) {
  const router = useRouter();
  const [authorized, setAuthorized] = useState(false);
  const [role, setRole] = useState<string | null>(null);

  useEffect(() => {}, []);

  useEffect(() => {
    const data = getTokenData();
    if (data?.rol) {
      if (requiredRole && data.rol !== requiredRole) {
        router.replace("/unauthorized");
        return;
      }
      setRole(data.rol);
    } else {
      router.replace("/login");
      return;
    }

    setAuthorized(true);
  }, [router, requiredRole, role]);

  if (!authorized) {
    return (
      <div className="flex justify-center items-center h-screen">
        <p className="text-gray-600 text-lg">Verificando sesión...</p>
      </div>
    );
  }

  return <>{children}</>;
}
