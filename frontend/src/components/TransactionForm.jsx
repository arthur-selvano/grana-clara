
export default function TransactionForm({
  form,
  handleChange,
  handleSubmit,
  submitting,
  formError,
  success,
  editingTransaction,
  handleCancelEdit,
}) {
  const isEditing = Boolean(editingTransaction)

  return (
    <form
      onSubmit={handleSubmit}
      className="mt-8 rounded-2xl border border-gray-100 bg-white p-6 shadow-sm"
    >
      <div className="flex flex-wrap items-center justify-between gap-3">
        <h2 className="text-lg font-semibold text-gray-900">
          {isEditing ? 'Editar transação' : 'Nova transação'}
        </h2>

        {isEditing && (
          <span className="rounded-full bg-amber-100 px-3 py-1 text-xs font-semibold text-amber-800">
            Modo de edição
          </span>
        )}
      </div>

      <div className="mt-5 grid grid-cols-1 gap-5 sm:grid-cols-2">
        <label className="text-sm font-medium text-gray-700">
          Descrição
          <input
            name="description"
            value={form.description}
            onChange={handleChange}
            required
            maxLength={200}
            placeholder="Ex.: Salário, mercado..."
            className="mt-2 w-full rounded-lg border border-gray-300 px-3 py-2.5 outline-none focus:border-emerald-600 focus:ring-2 focus:ring-emerald-100"
          />
        </label>

        <label className="text-sm font-medium text-gray-700">
          Valor (R$)
          <input
            name="amount"
            type="number"
            value={form.amount}
            onChange={handleChange}
            required
            min="0.01"
            step="0.01"
            placeholder="0,00"
            className="mt-2 w-full rounded-lg border border-gray-300 px-3 py-2.5 outline-none focus:border-emerald-600 focus:ring-2 focus:ring-emerald-100"
          />
        </label>

        <label className="text-sm font-medium text-gray-700">
          Data
          <input
            name="date"
            type="date"
            value={form.date}
            onChange={handleChange}
            required
            className="mt-2 w-full rounded-lg border border-gray-300 px-3 py-2.5 outline-none focus:border-emerald-600 focus:ring-2 focus:ring-emerald-100"
          />
        </label>

        <label className="text-sm font-medium text-gray-700">
          Tipo
          <select
            name="type"
            value={form.type}
            onChange={handleChange}
            className="mt-2 w-full rounded-lg border border-gray-300 bg-white px-3 py-2.5 outline-none focus:border-emerald-600 focus:ring-2 focus:ring-emerald-100"
          >
            <option value="INCOME">Receita</option>
            <option value="EXPENSE">Despesa</option>
          </select>
        </label>
      </div>

      {formError && (
        <p role="alert" className="mt-4 text-sm text-rose-600">
          {formError}
        </p>
      )}

      {success && (
        <p role="status" className="mt-4 text-sm text-emerald-700">
          {success}
        </p>
      )}

      <div className="mt-6 flex flex-wrap gap-3">
        <button
          type="submit"
          disabled={submitting}
          className="rounded-lg bg-emerald-700 px-5 py-3 text-sm font-semibold text-white transition hover:bg-emerald-800 disabled:cursor-not-allowed disabled:opacity-60"
        >
          {submitting
            ? 'Salvando...'
            : isEditing
              ? 'Salvar alterações'
              : 'Cadastrar transação'}
        </button>

        {isEditing && (
          <button
            type="button"
            onClick={handleCancelEdit}
            disabled={submitting}
            className="rounded-lg border border-gray-300 px-5 py-3 text-sm font-semibold text-gray-700 transition hover:bg-gray-50 disabled:cursor-not-allowed disabled:opacity-60"
          >
            Cancelar edição
          </button>
        )}
      </div>
    </form>
  )
}
