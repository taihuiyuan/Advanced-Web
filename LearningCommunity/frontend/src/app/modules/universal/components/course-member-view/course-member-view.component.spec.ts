import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CourseMemberViewComponent } from './course-member-view.component';

describe('TeacherCourseMemberViewComponent', () => {
  let component: CourseMemberViewComponent;
  let fixture: ComponentFixture<CourseMemberViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CourseMemberViewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CourseMemberViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
