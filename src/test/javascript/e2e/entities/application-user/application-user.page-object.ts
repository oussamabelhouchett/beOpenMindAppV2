import { element, by, ElementFinder } from 'protractor';

export class ApplicationUserComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-application-user div table .btn-danger'));
  title = element.all(by.css('jhi-application-user div h2#page-heading span')).first();
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

export class ApplicationUserUpdatePage {
  pageTitle = element(by.id('jhi-application-user-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  additionalFieldInput = element(by.id('field_additionalField'));

  userSelect = element(by.id('field_user'));
  postSelect = element(by.id('field_post'));
  commentsSelect = element(by.id('field_comments'));
  reactionSelect = element(by.id('field_reaction'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setAdditionalFieldInput(additionalField: string): Promise<void> {
    await this.additionalFieldInput.sendKeys(additionalField);
  }

  async getAdditionalFieldInput(): Promise<string> {
    return await this.additionalFieldInput.getAttribute('value');
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

  async commentsSelectLastOption(): Promise<void> {
    await this.commentsSelect.all(by.tagName('option')).last().click();
  }

  async commentsSelectOption(option: string): Promise<void> {
    await this.commentsSelect.sendKeys(option);
  }

  getCommentsSelect(): ElementFinder {
    return this.commentsSelect;
  }

  async getCommentsSelectedOption(): Promise<string> {
    return await this.commentsSelect.element(by.css('option:checked')).getText();
  }

  async reactionSelectLastOption(): Promise<void> {
    await this.reactionSelect.all(by.tagName('option')).last().click();
  }

  async reactionSelectOption(option: string): Promise<void> {
    await this.reactionSelect.sendKeys(option);
  }

  getReactionSelect(): ElementFinder {
    return this.reactionSelect;
  }

  async getReactionSelectedOption(): Promise<string> {
    return await this.reactionSelect.element(by.css('option:checked')).getText();
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

export class ApplicationUserDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-applicationUser-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-applicationUser'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
