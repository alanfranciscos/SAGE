import { bootstrapApplication } from '@angular/platform-browser';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideToastr } from 'ngx-toastr';
import { AppComponent } from './app/app.component';
import { appConfig } from './app/app.config';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './app/interceptors/auth.interceptor';

bootstrapApplication(AppComponent, {
  ...appConfig,
  providers: [
    ...appConfig.providers,
    provideAnimations(),
    provideToastr(), 
    provideAnimationsAsync(),
    provideHttpClient(withInterceptors([authInterceptor])),
  ]
}).catch((err) => console.error(err));