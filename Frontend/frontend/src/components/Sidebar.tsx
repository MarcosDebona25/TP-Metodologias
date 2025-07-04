import Link from "next/link";
import { logout } from "@/lib/auth";

type Props = {
  role: string;
};

export default function Sidebar({ role }: Props) {
  return (
    <>
      {role !== null && (
        <aside className="w-50 bg-gray-800 text-white p-4 h-screen flex flex-col justify-between">
          <div>
            <h2 className="text-xl font-bold mb-4">Menú</h2>
            <ul className="space-y-2 pt-2">
              <li>
                <Link
                  href="/license/new"
                  className="block font-bold hover:underline"
                >
                  Nueva Licencia
                </Link>
              </li>
              <li>
                <Link
                  href="/license/renew"
                  className="block font-bold hover:underline"
                >
                  Renovar Licencia
                </Link>
              </li>
              <li>
                <Link
                  href="/license/search"
                  className="block font-bold hover:underline"
                >
                  Licencias Vigentes
                </Link>
              </li>
              <li>
                <Link
                  href="/license/expiring"
                  className="block font-bold hover:underline"
                >
                  Licencias a Expirar
                </Link>
              </li>
              <li>
                <Link
                  href="/person/new"
                  className="block font-bold hover:underline"
                >
                  Registrar Titular
                </Link>
              </li>
              <li>
                <Link
                  href="/person/update"
                  className="block font-bold hover:underline"
                >
                  Modificar Titular
                </Link>
              </li>

              {role === "ADMIN" && (
                <>
                  <li>
                    <Link
                      href="/user/new"
                      className="block font-bold hover:underline"
                    >
                      Registrar Usuario
                    </Link>
                  </li>
                  <li>
                    <Link
                      href="/user/update"
                      className="block font-bold hover:underline"
                    >
                      Modificar Usuario
                    </Link>
                  </li>
                </>
              )}
            </ul>
          </div>
          <ul>
            <li>
              <div className="p-2">
                <button
                  onClick={logout}
                  className="w-full bg-red-600 hover:bg-red-700 text-white py-2 rounded"
                >
                  Cerrar sesión
                </button>
              </div>
            </li>
          </ul>
        </aside>
      )}
    </>
  );
}
