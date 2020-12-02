import { element, by, ElementFinder } from 'protractor';

export class PostComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-post div table .btn-danger'));
  title = element.all(by.css('jhi-post div h2#page-heading span')).first();
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

export class PostUpdatePage {
  pageTitle = element(by.id('jhi-post-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  titleInput = element(by.id('field_title'));
  contentInput = element(by.id('field_content'));
  datePubInput = element(by.id('field_datePub'));
  timeInput = element(by.id('field_time'));
  isNameVisibaleInput = element(by.id('field_isNameVisibale'));
  isPhotoVisibaleInput = element(by.id('field_isPhotoVisibale'));
  nbreLikeInput = element(by.id('field_nbreLike'));
  nbreCommentsInput = element(by.id('field_nbreComments'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setTitleInput(title: string): Promise<void> {
    await this.titleInput.sendKeys(title);
  }

  async getTitleInput(): Promise<string> {
    return await this.titleInput.getAttribute('value');
  }

  async setContentInput(content: string): Promise<void> {
    await this.contentInput.sendKeys(content);
  }

  async getContentInput(): Promise<string> {
    return await this.contentInput.getAttribute('value');
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

  getIsNameVisibaleInput(): ElementFinder {
    return this.isNameVisibaleInput;
  }

  getIsPhotoVisibaleInput(): ElementFinder {
    return this.isPhotoVisibaleInput;
  }

  async setNbreLikeInput(nbreLike: string): Promise<void> {
    await this.nbreLikeInput.sendKeys(nbreLike);
  }

  async getNbreLikeInput(): Promise<string> {
    return await this.nbreLikeInput.getAttribute('value');
  }

  async setNbreCommentsInput(nbreComments: string): Promise<void> {
    await this.nbreCommentsInput.sendKeys(nbreComments);
  }

  async getNbreCommentsInput(): Promise<string> {
    return await this.nbreCommentsInput.getAttribute('value');
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

export class PostDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-post-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-post'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
