import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BeOpenMindAppV2TestModule } from '../../../test.module';
import { ApplicationUserComponent } from 'app/entities/application-user/application-user.component';
import { ApplicationUserService } from 'app/entities/application-user/application-user.service';
import { ApplicationUser } from 'app/shared/model/application-user.model';

describe('Component Tests', () => {
  describe('ApplicationUser Management Component', () => {
    let comp: ApplicationUserComponent;
    let fixture: ComponentFixture<ApplicationUserComponent>;
    let service: ApplicationUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BeOpenMindAppV2TestModule],
        declarations: [ApplicationUserComponent],
      })
        .overrideTemplate(ApplicationUserComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ApplicationUserComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ApplicationUserService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ApplicationUser(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.applicationUsers && comp.applicationUsers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
