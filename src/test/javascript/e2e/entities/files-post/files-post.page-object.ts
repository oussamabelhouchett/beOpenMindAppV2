import { element, by, ElementFinder } from 'protractor';

export class FilesPostComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-files-post div table .btn-danger'));
  title = element.all(by.css('jhi-files-post div h2#page-heading span')).first();
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

export class FilesPostUpdatePage {
  pageTitle = element(by.id('jhi-files-post-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  pathInput = element(by.id('field_path'));
  typeInput = element(by.id('field_type'));

  filesPostSelect = element(by.id('field_filesPost'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setPathInput(path: string): Promise<void> {
    await this.pathInput.sendKeys(path);
  }

  async getPathInput(): Promise<string> {
    return await this.pathInput.getAttribute('value');
  }

  async setTypeInput(type: string): Promise<void> {
    await this.typeInput.sendKeys(type);
  }

  async getTypeInput(): Promise<string> {
    return await this.typeInput.getAttribute('value');
  }

  async filesPostSelectLastOption(): Promise<void> {
    await this.filesPostSelect.all(by.tagName('option')).last().click();
  }

  async filesPostSelectOption(option: string): Promise<void> {
    await this.filesPostSelect.sendKeys(option);
  }

  getFilesPostSelect(): ElementFinder {
    return this.filesPostSelect;
  }

  async getFilesPostSelectedOption(): Promise<string> {
    return await this.filesPostSelect.element(by.css('option:checked')).getText();
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

export class FilesPostDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-filesPost-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-filesPost'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
