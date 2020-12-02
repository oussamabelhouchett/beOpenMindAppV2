import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { BeOpenMindAppV2TestModule } from '../../../test.module';
import { FilesPostUpdateComponent } from 'app/entities/files-post/files-post-update.component';
import { FilesPostService } from 'app/entities/files-post/files-post.service';
import { FilesPost } from 'app/shared/model/files-post.model';

describe('Component Tests', () => {
  describe('FilesPost Management Update Component', () => {
    let comp: FilesPostUpdateComponent;
    let fixture: ComponentFixture<FilesPostUpdateComponent>;
    let service: FilesPostService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BeOpenMindAppV2TestModule],
        declarations: [FilesPostUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(FilesPostUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FilesPostUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FilesPostService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new FilesPost(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new FilesPost();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
