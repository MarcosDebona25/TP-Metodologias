import Link from "next/link";

export default function Sidebar() {
  return (
    <aside className="w-64 h-screen bg-gray-800 text-white p-4">
      <h2 className="text-xl font-bold mb-6">Gestor de Licencias</h2>
      <nav className="space-y-2">
        <Link
          href="/license/new"
          className="block hover:bg-gray-700 p-2 rounded"
        >
          Nueva Licencia
        </Link>

        <Link
          href="/license/renew"
          className="block hover:bg-gray-700 p-2 rounded"
        >
          Renovar Licencia
        </Link>

        <Link
          href="/license/expiring"
          className="block hover:bg-gray-700 p-2 rounded"
        >
          Licencias a Expirar
        </Link>

        <Link
          href="/person/new"
          className="block hover:bg-gray-700 p-2 rounded"
        >
          Registrar Titular
        </Link>

        <Link
          href="/person/update"
          className="block hover:bg-gray-700 p-2 rounded"
        >
          Modificar Titular
        </Link>

        <Link href="/user/new" className="block hover:bg-gray-700 p-2 rounded">
          Registrar Usuario
        </Link>
      </nav>
    </aside>
  );
}
