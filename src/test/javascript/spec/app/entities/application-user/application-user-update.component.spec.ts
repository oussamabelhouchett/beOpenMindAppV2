import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { BeOpenMindAppV2TestModule } from '../../../test.module';
import { ApplicationUserUpdateComponent } from 'app/entities/application-user/application-user-update.component';
import { ApplicationUserService } from 'app/entities/application-user/application-user.service';
import { ApplicationUser } from 'app/shared/model/application-user.model';

describe('Component Tests', () => {
  describe('ApplicationUser Management Update Component', () => {
    let comp: ApplicationUserUpdateComponent;
    let fixture: ComponentFixture<ApplicationUserUpdateComponent>;
    let service: ApplicationUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BeOpenMindAppV2TestModule],
        declarations: [ApplicationUserUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ApplicationUserUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ApplicationUserUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ApplicationUserService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ApplicationUser(123);
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
        const entity = new ApplicationUser();
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
