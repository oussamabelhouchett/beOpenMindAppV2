import { element, by, ElementFinder } from 'protractor';

export class CommentsComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-comments div table .btn-danger'));
  title = element.all(by.css('jhi-comments div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getText();
  }
}

export class CommentsUpdatePage {
  pageTitle = element(by.id('jhi-comments-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  contentTextInput = element(by.id('field_contentText'));
  datePubInput = element(by.id('field_datePub'));
  timeInput = element(by.id('field_time'));

  parentSelect = element(by.id('field_parent'));
  userSelect = element(by.id('field_user'));
  postSelect = element(by.id('field_post'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setContentTextInput(contentText: string): Promise<void> {
    await this.contentTextInput.sendKeys(contentText);
  }

  async getContentTextInput(): Promise<string> {
    return await this.contentTextInput.getAttribute('value');
  }

  async setDatePubInput(datePub: string): Promise<void> {
    await this.datePubInput.sendKeys(datePub);
  }

  async getDatePubInput(): Promise<string> {
    return await this.datePubInput.getAttribute('value');
  }

  async setTimeInput(time: string): Promise<void> {
    await this.timeInput.sendKeys(time);
  }

  async getTimeInput(): Promise<string> {
    return await this.timeInput.getAttribute('value');
  }

  async parentSelectLastOption(): Promise<void> {
    await this.parentSelect.all(by.tagName('option')).last().click();
  }

  async parentSelectOption(option: string): Promise<void> {
    await this.parentSelect.sendKeys(option);
  }

  getParentSelect(): ElementFinder {
    return this.parentSelect;
  }

  async getParentSelectedOption(): Promise<string> {
    return await this.parentSelect.element(by.css('option:checked')).getText();
  }

  async userSelectLastOption(): Promise<void> {
    await this.userSelect.all(by.tagName('option')).last().click();
  }

  async userSelectOption(option: string): Promise<void> {
    await this.userSelect.sendKeys(option);
  }

  getUserSelect(): ElementFinder {
    return this.userSelect;
  }

  async getUserSelectedOption(): Promise<string> {
    return await this.userSelect.element(by.css('option:checked')).getText();
  }

  async postSelectLastOption(): Promise<void> {
    await this.postSelect.all(by.tagName('option')).last().click();
  }

  async postSelectOption(option: string): Promise<void> {
    await this.postSelect.sendKeys(option);
  }

  getPostSelect(): ElementFinder {
    return this.postSelect;
  }

  async getPostSelectedOption(): Promise<string> {
    return await this.postSelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class CommentsDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-comments-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-comments'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
