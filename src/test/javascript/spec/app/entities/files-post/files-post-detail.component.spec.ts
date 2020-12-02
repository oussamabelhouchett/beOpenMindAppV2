import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BeOpenMindAppV2TestModule } from '../../../test.module';
import { FilesPostDetailComponent } from 'app/entities/files-post/files-post-detail.component';
import { FilesPost } from 'app/shared/model/files-post.model';

describe('Component Tests', () => {
  describe('FilesPost Management Detail Component', () => {
    let comp: FilesPostDetailComponent;
    let fixture: ComponentFixture<FilesPostDetailComponent>;
    const route = ({ data: of({ filesPost: new FilesPost(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BeOpenMindAppV2TestModule],
        declarations: [FilesPostDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(FilesPostDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FilesPostDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load filesPost on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.filesPost).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
