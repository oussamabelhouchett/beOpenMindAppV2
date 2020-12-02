import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ReactionComponentsPage, ReactionDeleteDialog, ReactionUpdatePage } from './reaction.page-object';

const expect = chai.expect;

describe('Reaction e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let reactionComponentsPage: ReactionComponentsPage;
  let reactionUpdatePage: ReactionUpdatePage;
  let reactionDeleteDialog: ReactionDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Reactions', async () => {
    await navBarPage.goToEntity('reaction');
    reactionComponentsPage = new ReactionComponentsPage();
    await browser.wait(ec.visibilityOf(reactionComponentsPage.title), 5000);
    expect(await reactionComponentsPage.getTitle()).to.eq('Reactions');
    await browser.wait(ec.or(ec.visibilityOf(reactionComponentsPage.entities), ec.visibilityOf(reactionComponentsPage.noResult)), 1000);
  });

  it('should load create Reaction page', async () => {
    await reactionComponentsPage.clickOnCreateButton();
    reactionUpdatePage = new ReactionUpdatePage();
    expect(await reactionUpdatePage.getPageTitle()).to.eq('Create or edit a Reaction');
    await reactionUpdatePage.cancel();
  });

  it('should create and save Reactions', async () => {
    const nbButtonsBeforeCreate = await reactionComponentsPage.countDeleteButtons();

    await reactionComponentsPage.clickOnCreateButton();

    await promise.all([
      reactionUpdatePage.setIsCommentInput('5'),
      reactionUpdatePage.setIsLikeInput('5'),
      reactionUpdatePage.postSelectLastOption(),
      reactionUpdatePage.userSelectLastOption(),
    ]);

    expect(await reactionUpdatePage.getIsCommentInput()).to.eq('5', 'Expected isComment value to be equals to 5');
    expect(await reactionUpdatePage.getIsLikeInput()).to.eq('5', 'Expected isLike value to be equals to 5');

    await reactionUpdatePage.save();
    expect(await reactionUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await reactionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Reaction', async () => {
    const nbButtonsBeforeDelete = await reactionComponentsPage.countDeleteButtons();
    await reactionComponentsPage.clickOnLastDeleteButton();

    reactionDeleteDialog = new ReactionDeleteDialog();
    expect(await reactionDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Reaction?');
    await reactionDeleteDialog.clickOnConfirmButton();

    expect(await reactionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
