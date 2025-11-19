import { Component, OnDestroy, OnInit } from '@angular/core';
import { SidebarComponent } from './sidebar/sidebar.component';
import { Subscription } from 'rxjs';
import { SseMessage, SseService } from '../../controller/sse/sse.service';
import { AccessibilityService } from '../../controller/accessibility/accessibility.service';

@Component({
  selector: 'app-main',
  standalone: true,
  imports: [SidebarComponent],
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss'],
})
export class MainComponent implements OnInit, OnDestroy {
  private subscription?: Subscription;
  private accessibilitySubscription?: Subscription;
  messages: string[] = [];

  constructor(
    private sseService: SseService,
    private accessibilityService: AccessibilityService
  ) {}

  ngOnInit() {
    // Conexão SSE
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
      }
    );

    if (this.accessibilityService.isDaltonicModeEnabled()) {
      document.body.classList.add('daltonic-mode');
    }

    this.accessibilitySubscription =
      this.accessibilityService.daltonicMode$.subscribe((enabled: boolean) => {
        if (enabled) {
          document.body.classList.add('daltonic-mode');
        } else {
          document.body.classList.remove('daltonic-mode');
        }
      });
  }

  ngOnDestroy() {
    this.subscription?.unsubscribe();
    this.accessibilitySubscription?.unsubscribe();
  }
}
