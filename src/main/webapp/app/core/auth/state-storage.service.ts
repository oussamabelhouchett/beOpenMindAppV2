import { Injectable } from '@angular/core';
import { SessionStorageService } from 'ngx-webstorage';
import { Account } from 'app/core/user/account.model';

@Injectable({ providedIn: 'root' })
export class StateStorageService {
  private previousUrlKey = 'previousUrl';
  private currentAccount = 'CurrentAccount';

  constructor(private $sessionStorage: SessionStorageService) {}

  storeUrl(url: string): void {
    this.$sessionStorage.store(this.previousUrlKey, url);
  }
  storeAccount(account: Account | null): void {
    this.$sessionStorage.store(this.currentAccount, account);
  }

  getUrl(): string | null | undefined {
    return this.$sessionStorage.retrieve(this.previousUrlKey);
  }

  getCurrentAccount(): Account | null | undefined {
    return this.$sessionStorage.retrieve(this.currentAccount);
  }
  clearUrl(): void {
    this.$sessionStorage.clear(this.previousUrlKey);
  }
}
