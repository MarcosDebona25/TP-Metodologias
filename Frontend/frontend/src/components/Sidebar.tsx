import Link from "next/link";

type Props = {
  role: string;
};

export default function Sidebar({ role }: Props) {
  return (
    <aside className="w-64 bg-gray-800 text-white p-4">
      <h2 className="text-xl font-bold mb-4">Menú</h2>
      <ul className="space-y-2">
        <li>
          <Link href="/license/new" className="block hover:underline">
            Nueva Licencia
          </Link>
        </li>
        <li>
          <Link href="/license/renew" className="block hover:underline">
            Renovar Licencia
          </Link>
        </li>
        <li>
          <Link href="/license/list" className="block hover:underline">
            Listar Licencias
          </Link>
        </li>
        <li>
          <Link href="/person/new" className="block hover:underline">
            Registrar Persona
          </Link>
        </li>
        <li>
          <Link href="/person/edit" className="block hover:underline">
            Modificar Persona
          </Link>
        </li>

        {role === "ADMIN" && (
          <>
            <li>
              <Link href="/user/new" className="block hover:underline">
                Registrar Usuario
              </Link>
            </li>
            <li>
              <Link href="/user/edit" className="block hover:underline">
                Modificar Usuario
              </Link>
            </li>
          </>
        )}

        <li>
          <form action="/logout" method="POST">
            <button className="text-red-400 hover:underline mt-4">
              Cerrar sesión
            </button>
          </form>
        </li>
      </ul>
    </aside>
  );
}
