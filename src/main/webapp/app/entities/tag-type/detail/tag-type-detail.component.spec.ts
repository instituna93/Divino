import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TagTypeDetailComponent } from './tag-type-detail.component';

describe('TagType Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TagTypeDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TagTypeDetailComponent,
              resolve: { tagType: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(TagTypeDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load tagType on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TagTypeDetailComponent);

      // THEN
      expect(instance.tagType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
