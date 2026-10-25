
import { useEffect, useMemo, useState } from 'react'
import {
  Area,
  AreaChart,
  Bar,
  BarChart,
  CartesianGrid,
  Cell,
  Legend,
  Pie,
  PieChart,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from 'recharts'

import SummaryCard from '../components/SummaryCard'
import { api } from '../services/api'
import { formatCurrency, formatDate } from '../utils/formatters'

const CHART_COLORS = [
  '#059669',
  '#f59e0b',
  '#6366f1',
  '#f43f5e',
  '#0ea5e9',
  '#8b5cf6',
  '#14b8a6',
  '#f97316',
  '#64748b',
  '#84cc16',
]

function getLastSixMonths() {
  const months = []
  const today = new Date()

  for (let i = 5; i >= 0; i -= 1) {
    const date = new Date(
      today.getFullYear(),
      today.getMonth() - i,
      1,
    )

    months.push({
      key: `${date.getFullYear()}-${String(
        date.getMonth() + 1,
      ).padStart(2, '0')}`,
      name: date.toLocaleDateString('pt-BR', {
        month: 'short',
      }),
      income: 0,
      expenses: 0,
    })
  }

  return months
}

function formatAxisCurrency(value) {
  return new Intl.NumberFormat('pt-BR', {
    notation: 'compact',
    style: 'currency',
    currency: 'BRL',
    maximumFractionDigits: 1,
  }).format(value)
}

export default function Dashboard() {
  const [summary, setSummary] = useState(null)
  const [transactions, setTransactions] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    let active = true

    async function loadDashboard() {
      setLoading(true)
      setError('')

      try {
        const [summaryData, transactionsData] = await Promise.all([
          api.getSummary(),
          api.getTransactions(),
        ])

        if (active) {
          setSummary(summaryData)
          setTransactions(transactionsData)
        }
      } catch (err) {
        console.error('Erro ao carregar dashboard:', err)

        if (active) {
          setError(
            'Não foi possível carregar os dados financeiros.',
          )
        }
      } finally {
        if (active) {
          setLoading(false)
        }
      }
    }

    loadDashboard()

    return () => {
      active = false
    }
  }, [])

  // Agrupa receitas e despesas por mês.
  const monthlyData = useMemo(() => {
    const months = getLastSixMonths()
    const monthMap = new Map(
      months.map((month) => [month.key, month]),
    )

    transactions.forEach((transaction) => {
      if (!transaction.date) return

      const key = transaction.date.slice(0, 7)
      const month = monthMap.get(key)
      const amount = Number(transaction.amount)

      if (!month || !Number.isFinite(amount)) return

      if (transaction.type === 'INCOME') {
        month.income += amount
      } else if (transaction.type === 'EXPENSE') {
        month.expenses += amount
      }
    })

    return months
  }, [transactions])

  // Evolução acumulada dentro dos seis meses exibidos.
  const balanceData = useMemo(() => {
    let runningBalance = 0

    return monthlyData.map((month) => {
      runningBalance += month.income - month.expenses

      return {
        name: month.name,
        balance: Number(runningBalance.toFixed(2)),
      }
    })
  }, [monthlyData])

  // Soma as despesas que possuem a mesma descrição.
  const expensePieData = useMemo(() => {
    const expensesByDescription = new Map()

    transactions.forEach((transaction) => {
      if (transaction.type !== 'EXPENSE') return

      const description = String(
        transaction.description || '',
      ).trim()

      const amount = Number(transaction.amount)

      if (!description || !Number.isFinite(amount) || amount <= 0) {
        return
      }

      // Ignora diferenças entre maiúsculas e minúsculas
      // ao agrupar, mas preserva a descrição original na legenda.
      const key = description.toLocaleLowerCase('pt-BR')
      const existing = expensesByDescription.get(key)

      if (existing) {
        existing.value += amount
      } else {
        expensesByDescription.set(key, {
          name: description,
          value: amount,
        })
      }
    })

    return Array.from(expensesByDescription.values())
      .map((item) => ({
        ...item,
        value: Number(item.value.toFixed(2)),
      }))
      .sort((a, b) => b.value - a.value)
      .map((item, index) => ({
        ...item,
        color: CHART_COLORS[index % CHART_COLORS.length],
      }))
  }, [transactions])

  const totalExpensesByDescription = useMemo(
    () =>
      expensePieData.reduce(
        (total, item) => total + item.value,
        0,
      ),
    [expensePieData],
  )

  const recentTransactions = useMemo(() => {
    return [...transactions]
      .sort((a, b) =>
        (b.date || '').localeCompare(a.date || ''),
      )
      .slice(0, 5)
  }, [transactions])

  if (loading) {
    return (
      <section>
        <h1 className="text-2xl font-bold text-emerald-950">
          Visão geral
        </h1>

        <p className="mt-4 text-sm text-gray-500">
          Carregando seu painel financeiro...
        </p>
      </section>
    )
  }

  if (error || !summary) {
    return (
      <section>
        <h1 className="text-2xl font-bold text-emerald-950">
          Visão geral
        </h1>

        <p
          role="alert"
          className="mt-4 rounded-xl bg-rose-50 p-4 text-sm text-rose-700"
        >
          {error || 'Não foi possível carregar o resumo financeiro.'}
        </p>

        <button
          type="button"
          onClick={() => window.location.reload()}
          className="mt-4 rounded-lg bg-emerald-700 px-4 py-2 text-sm font-semibold text-white hover:bg-emerald-800"
        >
          Tentar novamente
        </button>
      </section>
    )
  }

  return (
    <section className="space-y-8">
      {/* Cabeçalho */}
      <div>
        <p className="text-sm font-medium text-emerald-700">
          SEU DINHEIRO, SOB CONTROLE
        </p>

        <h1 className="mt-1 text-3xl font-bold tracking-tight text-emerald-950">
          Visão geral
        </h1>

        <p className="mt-2 text-gray-500">
          Acompanhe suas receitas, despesas e evolução financeira.
        </p>
      </div>

      {/* Cards de resumo */}
      <div className="grid grid-cols-1 gap-5 sm:grid-cols-2 xl:grid-cols-3">
        <SummaryCard
          title="Total de receitas"
          amount={formatCurrency(summary.totalIncome)}
          icon="↑"
          color="text-emerald-600"
        />

        <SummaryCard
          title="Total de despesas"
          amount={formatCurrency(summary.totalExpenses)}
          icon="↓"
          color="text-rose-600"
        />

        <SummaryCard
          title="Saldo atual"
          amount={formatCurrency(summary.balance)}
          icon="◈"
          color={
            Number(summary.balance) >= 0
              ? 'text-emerald-600'
              : 'text-rose-600'
          }
        />
      </div>

      {/* Gráficos mensais */}
      <div className="grid grid-cols-1 gap-6 xl:grid-cols-2">
        {/* Receitas x despesas */}
        <article className="min-w-0 rounded-2xl border border-gray-100 bg-white p-5 shadow-sm sm:p-6">
          <div className="mb-6">
            <h2 className="text-lg font-semibold text-gray-900">
              Receitas x despesas
            </h2>

            <p className="mt-1 text-sm text-gray-500">
              Comparativo mensal dos últimos seis meses
            </p>
          </div>

          <div className="h-72 w-full">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart
                data={monthlyData}
                margin={{ top: 8, right: 8, left: 0, bottom: 0 }}
              >
                <CartesianGrid
                  strokeDasharray="3 3"
                  vertical={false}
                />

                <XAxis
                  dataKey="name"
                  tickLine={false}
                  axisLine={false}
                />

                <YAxis
                  tickFormatter={formatAxisCurrency}
                  tickLine={false}
                  axisLine={false}
                  width={76}
                />

                <Tooltip
                  formatter={(value, name) => [
                    formatCurrency(Number(value)),
                    name === 'income' ? 'Receitas' : 'Despesas',
                  ]}
                  labelFormatter={(label) => `Mês: ${label}`}
                />

                <Legend
                  formatter={(value) =>
                    value === 'income' ? 'Receitas' : 'Despesas'
                  }
                />

                <Bar
                  dataKey="income"
                  fill="#059669"
                  radius={[5, 5, 0, 0]}
                />

                <Bar
                  dataKey="expenses"
                  fill="#f43f5e"
                  radius={[5, 5, 0, 0]}
                />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </article>

        {/* Evolução do saldo */}
        <article className="min-w-0 rounded-2xl border border-gray-100 bg-white p-5 shadow-sm sm:p-6">
          <div className="mb-6">
            <h2 className="text-lg font-semibold text-gray-900">
              Evolução do saldo
            </h2>

            <p className="mt-1 text-sm text-gray-500">
              Resultado acumulado dentro do período exibido
            </p>
          </div>

          <div className="h-72 w-full">
            <ResponsiveContainer width="100%" height="100%">
              <AreaChart
                data={balanceData}
                margin={{ top: 8, right: 8, left: 0, bottom: 0 }}
              >
                <defs>
                  <linearGradient
                    id="balanceFill"
                    x1="0"
                    y1="0"
                    x2="0"
                    y2="1"
                  >
                    <stop
                      offset="0%"
                      stopColor="#059669"
                      stopOpacity={0.25}
                    />
                    <stop
                      offset="95%"
                      stopColor="#059669"
                      stopOpacity={0.02}
                    />
                  </linearGradient>
                </defs>

                <CartesianGrid
                  strokeDasharray="3 3"
                  vertical={false}
                />

                <XAxis
                  dataKey="name"
                  tickLine={false}
                  axisLine={false}
                />

                <YAxis
                  tickFormatter={formatAxisCurrency}
                  tickLine={false}
                  axisLine={false}
                  width={76}
                />

                <Tooltip
                  formatter={(value) => [
                    formatCurrency(Number(value)),
                    'Saldo acumulado',
                  ]}
                  labelFormatter={(label) => `Mês: ${label}`}
                />

                <Area
                  type="monotone"
                  dataKey="balance"
                  name="Saldo acumulado"
                  stroke="#059669"
                  strokeWidth={3}
                  fill="url(#balanceFill)"
                  connectNulls
                />
              </AreaChart>
            </ResponsiveContainer>
          </div>
        </article>
      </div>

      {/* Pizza: despesas por descrição */}
      <article className="min-w-0 rounded-2xl border border-gray-100 bg-white p-5 shadow-sm sm:p-6">
        <div className="mb-5">
          <h2 className="text-lg font-semibold text-gray-900">
            Para onde vai seu dinheiro?
          </h2>

          <p className="mt-1 text-sm text-gray-500">
            Distribuição das despesas por descrição cadastrada
          </p>
        </div>

        {expensePieData.length === 0 ? (
          <p className="py-12 text-center text-sm text-gray-500">
            Cadastre despesas para visualizar sua distribuição financeira.
          </p>
        ) : (
          <div className="grid grid-cols-1 items-center gap-6 lg:grid-cols-[minmax(0,1.2fr)_minmax(0,1fr)]">
            <div className="mx-auto h-72 w-full min-w-0 max-w-md">
              <ResponsiveContainer width="100%" height="100%">
                <PieChart>
                  <Pie
                    data={expensePieData}
                    dataKey="value"
                    nameKey="name"
                    cx="50%"
                    cy="50%"
                    innerRadius={65}
                    outerRadius={105}
                    paddingAngle={2}
                    stroke="none"
                  >
                    {expensePieData.map((item) => (
                      <Cell
                        key={item.name.toLocaleLowerCase('pt-BR')}
                        fill={item.color}
                      />
                    ))}
                  </Pie>

                  <Tooltip
                    formatter={(value, name) => [
                      formatCurrency(Number(value)),
                      name,
                    ]}
                  />
                </PieChart>
              </ResponsiveContainer>
            </div>

            <div className="space-y-4">
              <div className="mb-5 border-b border-gray-100 pb-4">
                <p className="text-sm text-gray-500">
                  Total das despesas listadas
                </p>

                <p className="mt-1 text-2xl font-bold text-gray-900">
                  {formatCurrency(totalExpensesByDescription)}
                </p>
              </div>

              <div className="max-h-80 space-y-4 overflow-y-auto pr-2">
                {expensePieData.map((item) => {
                  const percentage =
                    totalExpensesByDescription > 0
                      ? (item.value / totalExpensesByDescription) * 100
                      : 0

                  return (
                    <div
                      key={item.name.toLocaleLowerCase('pt-BR')}
                      className="flex items-center gap-3"
                    >
                      <span
                        className="h-3 w-3 shrink-0 rounded-full"
                        style={{ backgroundColor: item.color }}
                      />

                      <div className="min-w-0 flex-1">
                        <p className="break-words text-sm text-gray-600">
                          {item.name}
                        </p>

                        <p className="font-semibold text-gray-900">
                          {formatCurrency(item.value)}
                        </p>
                      </div>

                      <span className="shrink-0 text-sm font-semibold text-gray-700">
                        {percentage.toLocaleString('pt-BR', {
                          maximumFractionDigits: 1,
                        })}
                        %
                      </span>
                    </div>
                  )
                })}
              </div>
            </div>
          </div>
        )}
      </article>

      {/* Transações recentes */}
      <article className="overflow-hidden rounded-2xl border border-gray-100 bg-white shadow-sm">
        <div className="flex flex-wrap items-center justify-between gap-3 border-b border-gray-100 px-6 py-5">
          <div>
            <h2 className="text-lg font-semibold text-gray-900">
              Transações recentes
            </h2>

            <p className="mt-1 text-sm text-gray-500">
              Suas últimas cinco movimentações
            </p>
          </div>
        </div>

        {recentTransactions.length === 0 ? (
          <p className="px-6 py-12 text-center text-sm text-gray-500">
            Nenhuma transação cadastrada ainda. Adicione sua primeira movimentação!
          </p>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-left text-sm">
              <thead className="bg-gray-50 text-gray-500">
                <tr>
                  <th className="px-6 py-3 font-medium">
                    Descrição
                  </th>
                  <th className="px-6 py-3 font-medium">
                    Tipo
                  </th>
                  <th className="px-6 py-3 font-medium">
                    Data
                  </th>
                  <th className="px-6 py-3 text-right font-medium">
                    Valor
                  </th>
                </tr>
              </thead>

              <tbody className="divide-y divide-gray-100">
                {recentTransactions.map((transaction) => (
                  <tr key={transaction.id}>
                    <td className="px-6 py-4 font-medium text-gray-900">
                      {transaction.description}
                    </td>

                    <td className="px-6 py-4">
                      <span
                        className={`rounded-full px-2.5 py-1 text-xs font-semibold ${
                          transaction.type === 'INCOME'
                            ? 'bg-emerald-50 text-emerald-700'
                            : 'bg-rose-50 text-rose-700'
                        }`}
                      >
                        {transaction.type === 'INCOME'
                          ? 'Receita'
                          : 'Despesa'}
                      </span>
                    </td>

                    <td className="px-6 py-4 text-gray-600">
                      {formatDate(transaction.date)}
                    </td>

                    <td className="px-6 py-4 text-right font-semibold text-gray-900">
                      {formatCurrency(transaction.amount)}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </article>
    </section>
  )
}