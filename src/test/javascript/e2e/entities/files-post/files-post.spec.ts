import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { FilesPostComponentsPage, FilesPostDeleteDialog, FilesPostUpdatePage } from './files-post.page-object';

const expect = chai.expect;

describe('FilesPost e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let filesPostComponentsPage: FilesPostComponentsPage;
  let filesPostUpdatePage: FilesPostUpdatePage;
  let filesPostDeleteDialog: FilesPostDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load FilesPosts', async () => {
    await navBarPage.goToEntity('files-post');
    filesPostComponentsPage = new FilesPostComponentsPage();
    await browser.wait(ec.visibilityOf(filesPostComponentsPage.title), 5000);
    expect(await filesPostComponentsPage.getTitle()).to.eq('Files Posts');
    await browser.wait(ec.or(ec.visibilityOf(filesPostComponentsPage.entities), ec.visibilityOf(filesPostComponentsPage.noResult)), 1000);
  });

  it('should load create FilesPost page', async () => {
    await filesPostComponentsPage.clickOnCreateButton();
    filesPostUpdatePage = new FilesPostUpdatePage();
    expect(await filesPostUpdatePage.getPageTitle()).to.eq('Create or edit a Files Post');
    await filesPostUpdatePage.cancel();
  });

  it('should create and save FilesPosts', async () => {
    const nbButtonsBeforeCreate = await filesPostComponentsPage.countDeleteButtons();

    await filesPostComponentsPage.clickOnCreateButton();

    await promise.all([
      filesPostUpdatePage.setPathInput('path'),
      filesPostUpdatePage.setTypeInput('type'),
      filesPostUpdatePage.filesPostSelectLastOption(),
    ]);

    expect(await filesPostUpdatePage.getPathInput()).to.eq('path', 'Expected Path value to be equals to path');
    expect(await filesPostUpdatePage.getTypeInput()).to.eq('type', 'Expected Type value to be equals to type');

    await filesPostUpdatePage.save();
    expect(await filesPostUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await filesPostComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last FilesPost', async () => {
    const nbButtonsBeforeDelete = await filesPostComponentsPage.countDeleteButtons();
    await filesPostComponentsPage.clickOnLastDeleteButton();

    filesPostDeleteDialog = new FilesPostDeleteDialog();
    expect(await filesPostDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Files Post?');
    await filesPostDeleteDialog.clickOnConfirmButton();

    expect(await filesPostComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
