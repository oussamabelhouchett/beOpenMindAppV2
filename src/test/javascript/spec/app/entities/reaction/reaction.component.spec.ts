import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BeOpenMindAppV2TestModule } from '../../../test.module';
import { ReactionComponent } from 'app/entities/reaction/reaction.component';
import { ReactionService } from 'app/entities/reaction/reaction.service';
import { Reaction } from 'app/shared/model/reaction.model';

describe('Component Tests', () => {
  describe('Reaction Management Component', () => {
    let comp: ReactionComponent;
    let fixture: ComponentFixture<ReactionComponent>;
    let service: ReactionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BeOpenMindAppV2TestModule],
        declarations: [ReactionComponent],
      })
        .overrideTemplate(ReactionComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ReactionComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ReactionService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Reaction(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.reactions && comp.reactions[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
