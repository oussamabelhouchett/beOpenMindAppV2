import { element, by, ElementFinder } from 'protractor';

export class ReactionComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-reaction div table .btn-danger'));
  title = element.all(by.css('jhi-reaction div h2#page-heading span')).first();
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

export class ReactionUpdatePage {
  pageTitle = element(by.id('jhi-reaction-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  isCommentInput = element(by.id('field_isComment'));
  isLikeInput = element(by.id('field_isLike'));

  postSelect = element(by.id('field_post'));
  userSelect = element(by.id('field_user'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIsCommentInput(isComment: string): Promise<void> {
    await this.isCommentInput.sendKeys(isComment);
  }

  async getIsCommentInput(): Promise<string> {
    return await this.isCommentInput.getAttribute('value');
  }

  async setIsLikeInput(isLike: string): Promise<void> {
    await this.isLikeInput.sendKeys(isLike);
  }

  async getIsLikeInput(): Promise<string> {
    return await this.isLikeInput.getAttribute('value');
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

export class ReactionDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-reaction-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-reaction'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
