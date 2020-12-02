import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { CommentsComponentsPage, CommentsDeleteDialog, CommentsUpdatePage } from './comments.page-object';

const expect = chai.expect;

describe('Comments e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let commentsComponentsPage: CommentsComponentsPage;
  let commentsUpdatePage: CommentsUpdatePage;
  let commentsDeleteDialog: CommentsDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Comments', async () => {
    await navBarPage.goToEntity('comments');
    commentsComponentsPage = new CommentsComponentsPage();
    await browser.wait(ec.visibilityOf(commentsComponentsPage.title), 5000);
    expect(await commentsComponentsPage.getTitle()).to.eq('Comments');
    await browser.wait(ec.or(ec.visibilityOf(commentsComponentsPage.entities), ec.visibilityOf(commentsComponentsPage.noResult)), 1000);
  });

  it('should load create Comments page', async () => {
    await commentsComponentsPage.clickOnCreateButton();
    commentsUpdatePage = new CommentsUpdatePage();
    expect(await commentsUpdatePage.getPageTitle()).to.eq('Create or edit a Comments');
    await commentsUpdatePage.cancel();
  });

  it('should create and save Comments', async () => {
    const nbButtonsBeforeCreate = await commentsComponentsPage.countDeleteButtons();

    await commentsComponentsPage.clickOnCreateButton();

    await promise.all([
      commentsUpdatePage.setContentTextInput('contentText'),
      commentsUpdatePage.setDatePubInput('2000-12-31'),
      commentsUpdatePage.setTimeInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      commentsUpdatePage.parentSelectLastOption(),
      commentsUpdatePage.userSelectLastOption(),
      commentsUpdatePage.postSelectLastOption(),
    ]);

    expect(await commentsUpdatePage.getContentTextInput()).to.eq('contentText', 'Expected ContentText value to be equals to contentText');
    expect(await commentsUpdatePage.getDatePubInput()).to.eq('2000-12-31', 'Expected datePub value to be equals to 2000-12-31');
    expect(await commentsUpdatePage.getTimeInput()).to.contain('2001-01-01T02:30', 'Expected time value to be equals to 2000-12-31');

    await commentsUpdatePage.save();
    expect(await commentsUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await commentsComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Comments', async () => {
    const nbButtonsBeforeDelete = await commentsComponentsPage.countDeleteButtons();
    await commentsComponentsPage.clickOnLastDeleteButton();

    commentsDeleteDialog = new CommentsDeleteDialog();
    expect(await commentsDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Comments?');
    await commentsDeleteDialog.clickOnConfirmButton();

    expect(await commentsComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
