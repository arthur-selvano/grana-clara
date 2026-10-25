export default function SummaryCard({ title, amount, icon, color }) {
  return (
    <article className="rounded-2xl border border-gray-100 bg-white p-5 shadow-sm">
      <div className="flex items-center justify-between">
        <p className="text-sm font-medium text-gray-500">
          {title}
        </p>

        <span className={`text-xl ${color}`} aria-hidden="true">
          {icon}
        </span>
      </div>

      <p className="mt-4 text-2xl font-bold tracking-tight text-gray-900">
        {amount}
      </p>
    </article>
  )
}
