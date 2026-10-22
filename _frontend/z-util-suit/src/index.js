/**
 * z-util-suit 的公开出口（lead 008 §7）。
 *
 * 为什么需要这个 barrel：本壳是 `private: true` 的应用层，package.json 里没有 exports 桶，
 * 仓外拿不到任何页面。这里把页面清单整份转发，并让文档页以**统一名字** `ComponentDocPage`
 * 暴露出去，宿主/主壳可以按同一个名字取，不必逐仓改名字。
 */
export * from './pages-manifest.jsx'
