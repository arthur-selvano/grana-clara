
import TransactionForm from '../components/TransactionForm'
import useTransactions from '../hooks/useTransactions'
import { formatCurrency, formatDate } from '../utils/formatters'

export default function Transactions() {
  const {
    transactions,
    form,
    loading,
    submitting,
    listError,
    formError,
    success,
    handleChange,
    handleSubmit,
    filteredTransactions,
    search,
    setSearch,
    typeFilter,
    setTypeFilter,
    startDate,
    setStartDate,
    endDate,
    setEndDate,
    editingTransaction,
    handleEdit,
    handleCancelEdit,
    handleDeleteTransaction,
    actionError,
    actionSuccess,
  } = useTransactions()

  return (
    <section>
      <div>
        <h1 className="text-2xl font-bold text-emerald-950">
          Transações
        </h1>

        <p className="mt-2 text-gray-500">
          Cadastre e acompanhe suas receitas e despesas.
        </p>
      </div>

      <TransactionForm
        form={form}
        handleChange={handleChange}
        handleSubmit={handleSubmit}
        editingTransaction={editingTransaction}
        handleCancelEdit={handleCancelEdit}
        submitting={submitting}
        formError={formError}
        success={success}
      />

      {actionError && (
        <p
          role="alert"
          className="mt-4 rounded-lg bg-rose-50 p-3 text-sm text-rose-700"
        >
          {actionError}
        </p>
      )}

      {actionSuccess && (
        <p
          role="status"
          className="mt-4 rounded-lg bg-emerald-50 p-3 text-sm text-emerald-700"
        >
          {actionSuccess}
        </p>
      )}

      <div className="mt-8 rounded-2xl border border-gray-100 bg-white p-6 shadow-sm">
        <h2 className="text-lg font-semibold text-gray-900">
          Filtrar transações
        </h2>

        <div className="mt-5 grid grid-cols-1 gap-4 sm:grid-cols-2 xl:grid-cols-4">
          <label className="text-sm font-medium text-gray-700">
            Pesquisar descrição
            <input
              type="search"
              value={search}
              onChange={(event) => setSearch(event.target.value)}
              placeholder="Ex.: mercado, salário..."
              className="mt-2 w-full rounded-lg border border-gray-300 px-3 py-2.5 outline-none focus:border-emerald-600 focus:ring-2 focus:ring-emerald-100"
            />
          </label>

          <label className="text-sm font-medium text-gray-700">
            Tipo
            <select
              value={typeFilter}
              onChange={(event) => setTypeFilter(event.target.value)}
              className="mt-2 w-full rounded-lg border border-gray-300 bg-white px-3 py-2.5 outline-none focus:border-emerald-600 focus:ring-2 focus:ring-emerald-100"
            >
              <option value="ALL">Todos os tipos</option>
              <option value="INCOME">Receitas</option>
              <option value="EXPENSE">Despesas</option>
            </select>
          </label>

          <label className="text-sm font-medium text-gray-700">
            Data inicial
            <input
              type="date"
              value={startDate}
              max={endDate || undefined}
              onChange={(event) => setStartDate(event.target.value)}
              className="mt-2 w-full rounded-lg border border-gray-300 px-3 py-2.5 outline-none focus:border-emerald-600 focus:ring-2 focus:ring-emerald-100"
            />
          </label>

          <label className="text-sm font-medium text-gray-700">
            Data final
            <input
              type="date"
              value={endDate}
              min={startDate || undefined}
              onChange={(event) => setEndDate(event.target.value)}
              className="mt-2 w-full rounded-lg border border-gray-300 px-3 py-2.5 outline-none focus:border-emerald-600 focus:ring-2 focus:ring-emerald-100"
            />
          </label>
        </div>

        <div className="mt-4 flex flex-wrap items-center justify-between gap-3">
          <p className="text-sm text-gray-500">
            {filteredTransactions.length}{' '}
            {filteredTransactions.length === 1
              ? 'transação encontrada'
              : 'transações encontradas'}
          </p>

          <button
            type="button"
            onClick={() => {
              setSearch('')
              setTypeFilter('ALL')
              setStartDate('')
              setEndDate('')
            }}
            className="rounded-lg border border-gray-300 px-4 py-2 text-sm font-medium text-gray-700 transition hover:bg-gray-50"
          >
            Limpar filtros
          </button>
        </div>
      </div>

      <div className="mt-8 overflow-hidden rounded-2xl border border-gray-100 bg-white shadow-sm">
        <div className="border-b border-gray-100 px-6 py-4">
          <h2 className="font-semibold text-gray-900">
            Todas as movimentações
          </h2>
        </div>

        {listError && (
          <div
            role="alert"
            className="border-b border-rose-100 bg-rose-50 px-6 py-3 text-sm text-rose-700"
          >
            {listError}
          </div>
        )}

        {loading ? (
          <p className="p-6 text-sm text-gray-500">
            Carregando transações...
          </p>
        ) : listError && transactions.length === 0 ? (
          <p className="p-6 text-sm text-gray-500">
            Não foi possível exibir as transações. Tente recarregar a página.
          </p>
        ) : filteredTransactions.length === 0 ? (
          <p className="px-6 py-12 text-center text-sm text-gray-500">
            {transactions.length === 0
              ? 'Nenhuma transação cadastrada. Adicione sua primeira movimentação acima.'
              : 'Nenhuma transação corresponde aos filtros selecionados.'}
          </p>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-left text-sm">
              <thead className="bg-gray-50 text-gray-500">
                <tr>
                  <th className="px-6 py-3 font-medium">Descrição</th>
                  <th className="px-6 py-3 font-medium">Tipo</th>
                  <th className="px-6 py-3 font-medium">Data</th>
                  <th className="px-6 py-3 text-right font-medium">Valor</th>
                  <th className="px-6 py-3 text-right font-medium">Ações</th>
                </tr>
              </thead>

              <tbody className="divide-y divide-gray-100">
                {filteredTransactions.map((transaction) => (
                  <tr key={transaction.id}>
                    <td className="px-6 py-4 font-medium text-gray-900">
                      {transaction.description}
                    </td>

                    <td className="px-6 py-4 text-gray-600">
                      {transaction.type === 'INCOME'
                        ? 'Receita'
                        : 'Despesa'}
                    </td>

                    <td className="px-6 py-4 text-gray-600">
                      {formatDate(transaction.date)}
                    </td>

                    <td className="px-6 py-4 text-right font-semibold text-gray-900">
                      {formatCurrency(transaction.amount)}
                    </td>

                    <td className="px-6 py-4">
                      <div className="flex justify-end gap-2">
                        <button
                          type="button"
                          onClick={() => handleEdit(transaction)}
                          className="rounded-lg border border-emerald-200 px-3 py-2 text-sm font-medium text-emerald-700 transition hover:bg-emerald-50"
                          aria-label={`Editar ${transaction.description}`}
                        >
                          Editar
                        </button>

                        <button
                          type="button"
                          onClick={() => {
                            const confirmed = window.confirm(
                              `Tem certeza que deseja excluir "${transaction.description}"?`,
                            )

                            if (confirmed) {
                              handleDeleteTransaction(transaction.id)
                            }
                          }}
                          className="rounded-lg border border-rose-200 px-3 py-2 text-sm font-medium text-rose-700 transition hover:bg-rose-50"
                          aria-label={`Excluir ${transaction.description}`}
                        >
                          Excluir
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </section>
  )
}