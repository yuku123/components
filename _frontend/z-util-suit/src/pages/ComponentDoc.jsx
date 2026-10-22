import {ComponentDoc} from '@yuku123/z-frontend-common/ui'
import docMarkdown from '../DOC.md?raw'

/**
 * 组件文档页（lead 008 §7）—— 文档内容来自本包 src/DOC.md。
 *
 * 为什么用 `?raw`：vite 编译期把 DOC.md 内联成字符串，零额外请求；
 * 文档随包发布，宿主不用管文件怎么放。
 */
export default function ComponentDocPage() {
    return <ComponentDoc markdown={docMarkdown} title="组件文档 — z-util" />
}
