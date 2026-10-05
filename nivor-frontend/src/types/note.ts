export type Note = { id:number;title:string;content:string;category:string|null;tags:string[];favorite:boolean;pinned:boolean;createdAt:string;updatedAt:string }
export type NoteInput = Pick<Note,'title'|'content'|'category'|'tags'> & Partial<Pick<Note,'favorite'|'pinned'>>
