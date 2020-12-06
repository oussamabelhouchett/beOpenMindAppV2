import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';

import { Component, OnInit, OnDestroy, ElementRef, AfterViewInit } from '@angular/core';
import { JhiEventManager } from 'ng-jhipster';
import { Account } from 'app/core/user/account.model';
import { LoginService } from 'app/core/login/login.service';
import { StateStorageService } from 'app/core/auth/state-storage.service';

@Component({
  selector: 'jhi-login',
  templateUrl: './login.component.html',
  styleUrls: ['login.scss'],
})
export class LoginComponent implements OnInit, AfterViewInit, OnDestroy {
  authenticationError = false;
  loginForm = this.fb.group({
    username: [''],
    password: [''],
    rememberMe: [false],
  });

  constructor(
    private eventManager: JhiEventManager,
    private loginService: LoginService,
    private stateStorageService: StateStorageService,
    private elementRef: ElementRef,
    private router: Router,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {}

  ngAfterViewInit(): void {}

  cancel(): void {
    this.authenticationError = false;
    this.loginForm.patchValue({
      username: '',
      password: '',
    });
  }

  login(): void {
    this.loginService
      .login({
        username: this.loginForm.get('username')!.value,
        password: this.loginForm.get('password')!.value,
        rememberMe: this.loginForm.get('rememberMe')!.value,
      })
      .subscribe(
        (res: Account | null) => {
          // eslint-disable-next-line no-console
          this.authenticationError = false;
          //   this.activeModal.dismiss('login success');
          if (
            this.router.url === '/account/register' ||
            this.router.url.startsWith('/account/activate') ||
            this.router.url.startsWith('/account/reset/')
          ) {
            this.router.navigate(['']);
          }

          this.eventManager.broadcast({
            name: 'authenticationSuccess',
            content: 'Sending Authentication Success',
          });
          if (res!.authorities.includes('ROLE_ADMIN')) this.router.navigateByUrl('/home');
          else this.router.navigateByUrl('/accueil');
          this.stateStorageService.storeAccount(res);
          // previousState was set in the authExpiredInterceptor before being redirected to login modal.
          // since login is successful, go to stored previousState and clear previousState
          const redirect = this.stateStorageService.getUrl();
          if (redirect) {
            this.stateStorageService.storeUrl('null');
            this.router.navigateByUrl(redirect);
          }
        },
        () => (this.authenticationError = true)
      );
  }

  register(): void {
    this.router.navigate(['/account/register']);
  }

  requestResetPassword(): void {
    this.router.navigate(['/account/reset', 'request']);
  }
  ngOnDestroy(): void {}
}
