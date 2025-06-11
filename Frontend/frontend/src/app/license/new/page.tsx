// app/license/new/page.tsx
export default function NewLicensePage() {
  return (
    <div>
      <h1 className="text-2xl font-bold mb-4">Crear Nueva Licencia</h1>
      <form className="space-y-4 max-w-md">
        <div>
          <label className="block font-medium">
            Tipo y Número de Documento
          </label>
          <input type="text" className="w-full border rounded px-3 py-2" />
        </div>
            <label className="block font-medium">Tipo y Número de Documento</label>
        <div className="flex gap-4">
          {/* Dropdown Field */}
          <div className="w-30">
            <select className="w-full border rounded px-3 py-2">
              <option value="">Seleccione</option>
              <option value="DNI">DNI</option>
              <option value="LE/LI">LE/LC</option>
              <option value="CI">CI</option>
            </select>
          </div>
          {/* Text Field */}
          <div className="flex-1">
            <input
              type="text"
              className="w-full border rounded px-3 py-2"
              placeholder="e.g. AB123456"
            />
          </div>
        </div>

        <div>
          <label className="block font-medium">Apellido</label>
          <input type="text" className="w-full border rounded px-3 py-2" />
        </div>
        <div>
          <label className="block font-medium">Nombre</label>
          <input type="text" className="w-full border rounded px-3 py-2" />
        </div>
        <div>
          <label className="block font-medium">Fecha de Nacimiento</label>
          <input type="date" className="w-full border rounded px-3 py-2" />
        </div>
        <div>
          <label className="block font-medium">Dirección</label>
          <input type="text" className="w-full border rounded px-3 py-2" />
        </div>
        <div>
          <label className="block font-medium">
            Grupo Sanguíneo y Factor RH
          </label>
          <input type="text" className="w-full border rounded px-3 py-2" />
        </div>
        <button
          type="submit"
          className="bg-blue-600 text-white px-4 py-2 rounded"
        >
          Submit
        </button>
      </form>
    </div>
  );
}
