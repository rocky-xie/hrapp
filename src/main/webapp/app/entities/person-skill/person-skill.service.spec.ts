import { beforeEach, describe, expect, it, vi } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';

import { DATE_FORMAT } from '@/shared/composables/date-format';
import { PersonSkill } from '@/shared/model/person-skill.model';

import PersonSkillService from './person-skill.service';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: vi.spyOn(axios, 'get'),
  post: vi.spyOn(axios, 'post'),
  put: vi.spyOn(axios, 'put'),
  patch: vi.spyOn(axios, 'patch'),
  delete: vi.spyOn(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('PersonSkill Service', () => {
    let service: PersonSkillService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new PersonSkillService();
      currentDate = new Date();
      elemDefault = new PersonSkill(123, currentDate, currentDate, 'AAAAAAA', 'HIGH', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          assessmentDate: dayjs(currentDate).format(DATE_FORMAT),
          nextReviewDate: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        axiosStub.get.mockResolvedValue({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.mockRejectedValue(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a PersonSkill', async () => {
        const returnedFromService = {
          id: 123,
          assessmentDate: dayjs(currentDate).format(DATE_FORMAT),
          nextReviewDate: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        const expected = { assessmentDate: currentDate, nextReviewDate: currentDate, ...returnedFromService };

        axiosStub.post.mockResolvedValue({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a PersonSkill', async () => {
        axiosStub.post.mockRejectedValue(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a PersonSkill', async () => {
        const returnedFromService = {
          assessmentDate: dayjs(currentDate).format(DATE_FORMAT),
          nextReviewDate: dayjs(currentDate).format(DATE_FORMAT),
          evidence: 'BBBBBB',
          confidence: 'BBBBBB',
          growthDirection: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { assessmentDate: currentDate, nextReviewDate: currentDate, ...returnedFromService };
        axiosStub.put.mockResolvedValue({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a PersonSkill', async () => {
        axiosStub.put.mockRejectedValue(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a PersonSkill', async () => {
        const patchObject = {
          assessmentDate: dayjs(currentDate).format(DATE_FORMAT),
          nextReviewDate: dayjs(currentDate).format(DATE_FORMAT),
          growthDirection: 'BBBBBB',
          ...new PersonSkill(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { assessmentDate: currentDate, nextReviewDate: currentDate, ...returnedFromService };
        axiosStub.patch.mockResolvedValue({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a PersonSkill', async () => {
        axiosStub.patch.mockRejectedValue(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of PersonSkill', async () => {
        const returnedFromService = {
          assessmentDate: dayjs(currentDate).format(DATE_FORMAT),
          nextReviewDate: dayjs(currentDate).format(DATE_FORMAT),
          evidence: 'BBBBBB',
          confidence: 'BBBBBB',
          growthDirection: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { assessmentDate: currentDate, nextReviewDate: currentDate, ...returnedFromService };
        axiosStub.get.mockResolvedValue([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of PersonSkill', async () => {
        axiosStub.get.mockRejectedValue(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a PersonSkill', async () => {
        axiosStub.delete.mockResolvedValue({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a PersonSkill', async () => {
        axiosStub.delete.mockRejectedValue(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
