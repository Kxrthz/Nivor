export type CalendarEvent = { id: number; title: string; description: string | null; startTime: string; endTime: string; location: string | null; color: string | null; allDay: boolean; recurrenceRule: string | null; createdAt: string; updatedAt: string }
export type CalendarEventInput = Omit<CalendarEvent,'id'|'createdAt'|'updatedAt'>
