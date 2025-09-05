// sse.service.ts
import { Injectable, NgZone } from '@angular/core';
import { Observable, ReplaySubject } from 'rxjs';

export interface SseMessage<T = any> {
  type: string; // nome do evento (ex.: 'assignment-change', 'connected' ou 'default')
  data: T; // payload já parseado quando possível
  raw: string; // texto bruto recebido
}

@Injectable({
  providedIn: 'root',
})
export class SseService {
  private eventSource?: EventSource;
  private messagesSubject = new ReplaySubject<SseMessage>(1); // guarda o último valor sem emitir null
  public messages$: Observable<SseMessage> =
    this.messagesSubject.asObservable();

  private isConnected = false;

  constructor(private zone: NgZone) {}

  /**
   * Abre a conexão SSE e registra listeners para eventos nomeados.
   * - Se `eventNames` estiver vazio, escuta apenas o evento default (onmessage).
   * - Caso contrário, escuta o default + cada evento listado.
   */
  connect(url: string, eventNames: string[] = []): void {
    if (this.eventSource) return;

    this.eventSource = new EventSource(url);

    // Evento "open" da conexão
    this.eventSource.onopen = () => {
      this.zone.run(() => {
        this.isConnected = true;
        this.messagesSubject.next({
          type: 'open',
          data: { connected: true },
          raw: 'SSE connection opened',
        });
      });
    };

    // Evento default (mensagens sem "event: <nome>")
    this.eventSource.onmessage = (event) => {
      this.zone.run(() => {
        const parsed = this.safeParse(event.data);
        this.messagesSubject.next({
          type: 'default',
          data: parsed ?? event.data,
          raw: event.data,
        });
      });
    };

    // Eventos nomeados vindos do servidor (ex.: 'connected', 'assignment-change', etc.)
    eventNames.forEach((evtName) => {
      this.eventSource!.addEventListener(evtName, (event: MessageEvent) => {
        this.zone.run(() => {
          const parsed = this.safeParse(event.data);
          this.messagesSubject.next({
            type: evtName,
            data: parsed ?? event.data,
            raw: event.data,
          });
        });
      });
    });

    // Erros / reconexões
    this.eventSource.onerror = (error) => {
      this.zone.run(() => {
        console.error('Erro SSE:', error);
        this.messagesSubject.next({
          type: 'error',
          data: { error: true },
          raw: 'SSE connection error',
        });
        // O EventSource tenta reconectar automaticamente; não "complete" o Subject.
        // Se quiser derrubar a conexão definitivamente, chame disconnect().
      });
    };
  }

  disconnect(): void {
    this.eventSource?.close();
    this.eventSource = undefined;
    this.isConnected = false;
    // NÃO dar complete(), para permitir reconectar depois.
    this.messagesSubject.next({
      type: 'close',
      data: { connected: false },
      raw: 'SSE connection closed',
    });
  }

  get connected(): boolean {
    return this.isConnected;
  }

  private safeParse(text: string) {
    try {
      return JSON.parse(text);
    } catch {
      return null;
    }
  }
}
