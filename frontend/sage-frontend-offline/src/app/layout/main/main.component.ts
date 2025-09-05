import { Component } from '@angular/core';
import { SidebarComponent } from './sidebar/sidebar.component';
import { Subscription } from 'rxjs';
import { SseMessage, SseService } from '../../controller/sse/sse.service';

@Component({
  selector: 'app-main',
  standalone: true,
  imports: [SidebarComponent],
  templateUrl: './main.component.html',
  styleUrl: './main.component.scss',
})
export class MainComponent {
  private subscription?: Subscription;
  messages: string[] = [];

  constructor(private sseService: SseService) {}

  ngOnInit() {
    this.sseService.connect('http://localhost:8080/api/v1/sse/assist', [
      'connected',
      'assignment-change',
    ]);

    this.subscription = this.sseService.messages$.subscribe(
      (msg: SseMessage) => {
        console.log('Main recebeu:', msg.type, msg.data);
        this.messages.push(
          `${msg.type}: ${
            typeof msg.data === 'string' ? msg.data : JSON.stringify(msg.data)
          }`
        );
        // Aqui você pode reagir por tipo:
        // if (msg.type === 'assignment-change') { this.fetchResidents(); }
      }
    );
  }

  ngOnDestroy() {
    this.subscription?.unsubscribe();
  }
}
