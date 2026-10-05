import { useEditor,EditorContent } from '@tiptap/react'
import StarterKit from '@tiptap/starter-kit'
import { Bold,Italic,List,Quote } from 'lucide-react'
export function RichTextEditor({onChange,initialContent=''}:{onChange:(html:string)=>void;initialContent?:string}){
 const editor=useEditor({extensions:[StarterKit],content:initialContent,editorProps:{attributes:{'aria-label':'Rich text content',class:'rich-editor-body'}} ,onUpdate:({editor:e})=>onChange(e.getHTML())})
 if(!editor)return <div className="rich-editor-loading" aria-label="Preparing editor"/>
 return <div className="rich-editor"><div className="rich-toolbar" role="toolbar" aria-label="Text formatting"><button type="button" aria-label="Bold" aria-pressed={editor.isActive('bold')} onClick={()=>editor.chain().focus().toggleBold().run()}><Bold size={14}/></button><button type="button" aria-label="Italic" aria-pressed={editor.isActive('italic')} onClick={()=>editor.chain().focus().toggleItalic().run()}><Italic size={14}/></button><button type="button" aria-label="Bullet list" aria-pressed={editor.isActive('bulletList')} onClick={()=>editor.chain().focus().toggleBulletList().run()}><List size={14}/></button><button type="button" aria-label="Block quote" aria-pressed={editor.isActive('blockquote')} onClick={()=>editor.chain().focus().toggleBlockquote().run()}><Quote size={14}/></button></div><EditorContent editor={editor}/></div>
}
